package lapr4.showproposal.csvprotocol.client;

import lapr4.showproposalmanagement.dto.ShowProposalDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Proxy that handles communication with the CSV - Customer App protocol server.
 *
 */
public class CsvCASProtocolProxy {
	private static final Logger LOGGER = LogManager.getLogger(CsvCASProtocolProxy.class);

	private static final String SERVERIP = "vs791.dei.isep.ipp.pt";
	private static boolean ServerIPConnectIsShow = false;

	/**
	 * Client socket for the CSV protocol.
	 */
	private static class ClientSocket {

		private Socket sock;
		private PrintWriter output;
		private BufferedReader input;

		/**
		 * Connects to the server at the specified address and port.
		 *
		 * @param address IP address or hostname of the server
		 * @param port Port number to connect to
		 *
		 * @throws IOException if an I/O error occurs when creating the socket or connecting
		 */
		public void connect(final String address, final int port) throws IOException {
			InetAddress serverIP;

			serverIP = InetAddress.getByName(address);

			sock = new Socket(serverIP, port);
			output = new PrintWriter(sock.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			if (!ServerIPConnectIsShow) {
				ServerIPConnectIsShow = true;
				LOGGER.debug("Connected to {}", address);
			}
		}

		/**
		 * Sends a request to the server.
		 *
		 * @param request Request string to be sent
		 */
		public void send(final String request) {
			output.println(request);
		}

		/**
		 * Receives a response from the server.
		 *
		 * @return List of response strings
		 *
		 * @throws IOException if an I/O error occurs while reading from the input stream
		 */
		public List<String> recv() throws IOException {
			final var resp = new ArrayList<String>();

			var eof = false;
			do {
				final String inputLine = input.readLine();
				if (inputLine != null) {
					if (inputLine.isEmpty()) {
						eof = true;
					} else {
						resp.add(inputLine);
					}
				}
			} while (!eof);


			return resp;
		}

		/**
		 * Sends a request and receives the response.
		 *
		 * @param request Request string to be sent
		 *
		 * @return List of response strings
		 *
		 * @throws IOException if an I/O error occurs while sending or receiving data
		 */
		public List<String> sendAndRecv(final String request) throws IOException {
			send(request);
			return recv();
		}

		/**
		 * Stops the client socket, closing the input and output streams and the socket.
		 *
		 * @throws IOException if an I/O error occurs while closing the streams or socket
		 */
		public void stop() throws IOException {
			input.close();
			output.close();
			sock.close();
		}
	}


	/**
	 * Lists the proposals made by a representative that are awaiting response.
	 *
	 * @return Iterable of ShowProposalDTO
	 *
	 * @throws IOException if an I/O error occurs
	 * @throws IllegalStateException if the connection is not established properly
	 * @throws FailedRequestException if the request fails
	 */
	public Iterable<ShowProposalDTO> listRepresentativeProposalsAwaitingResponse(final String username, String password)
			throws IllegalStateException, IOException, FailedRequestException {

		final var socket = new ClientSocket();
		socket.connect(getAddress(), getPort());

		final String request = new GetRepresentativeProposalsAwaitingResponseRequestDTO(username, password).toRequest();
		final List<String> response = socket.sendAndRecv(request);

		socket.stop();

		final CsvServerResponseParser srp = new CsvServerResponseParser();
		return srp.parseResponseMessageListCustomerShowProposals(response);
	}

	/**
	 * Accepts a proposal made by a representative.
	 *
	 * @return String message indicating the result of the operation
	 *
	 * @throws IOException if an I/O error occurs
	 * @throws IllegalStateException if the connection is not established properly
	 * @throws FailedRequestException if the request fails
	 */
	public String acceptProposal(final String username, String password, Long proposalId)
			throws IllegalStateException, IOException, FailedRequestException {

		final var socket = new ClientSocket();
		socket.connect(getAddress(), getPort());

		final String request = new AcceptProposalRequestDTO(username, password, proposalId).toRequest();
		final List<String> response = socket.sendAndRecv(request);

		socket.stop();

		final CsvServerResponseParser srp = new CsvServerResponseParser();
		return srp.parseResponseMessageAcceptProposal(response);
	}

	/**
	 * Rejects a proposal made by a representative.
	 *
	 * @return String message indicating the result of the operation
	 *
	 * @throws IOException if an I/O error occurs
	 * @throws IllegalStateException if the connection is not established properly
	 * @throws FailedRequestException if the request fails
	 */
	public String rejectProposal(final String username, String password, Long proposalId, String feedback)
			throws IllegalStateException, IOException, FailedRequestException {

		final var socket = new ClientSocket();
		socket.connect(getAddress(), getPort());

		final String request = new RejectProposalRequestDTO(username, password, proposalId, feedback).toRequest();
		final List<String> response = socket.sendAndRecv(request);

		socket.stop();

		final CsvServerResponseParser srp = new CsvServerResponseParser();
		return srp.parseResponseMessageRejectProposal(response);
	}

	/**
	 * Lists the shows scheduled by a customer.
	 *
	 * @return Iterable of ShowProposalDTO
	 *
	 * @throws IOException if an I/O error occurs
	 * @throws IllegalStateException if the connection is not established properly
	 * @throws FailedRequestException if the request fails
	 */
	public Iterable<ShowProposalDTO> listCustomerScheduledShows(final String username, String password)
			throws IllegalStateException, IOException, FailedRequestException {

		final var socket = new ClientSocket();
		socket.connect(getAddress(), getPort());

		final String request = new GetCustomerScheduledShowsRequestDTO(username, password).toRequest();
		final List<String> response = socket.sendAndRecv(request);

		socket.stop();

		final CsvServerResponseParser srp = new CsvServerResponseParser();
		return srp.parseResponseMessageListCustomerShowProposals(response);
	}

	/**
	 * Lists show proposals made by a customer.
	 *
	 * @return Iterable of ShowProposalDTO
	 *
	 * @throws IOException if an I/O error occurs
	 * @throws IllegalStateException if the connection is not established properly
	 * @throws FailedRequestException if the request fails
	 */
	public Iterable<ShowProposalDTO> listCustomerShowProposals(final String username, String password)
			throws IllegalStateException, IOException, FailedRequestException {

		final var socket = new ClientSocket();
		socket.connect(getAddress(), getPort());

		final String request = new GetCustomerShowProposalsRequestDTO(username, password).toRequest();
		final List<String> response = socket.sendAndRecv(request);

		socket.stop();

		final CsvServerResponseParser srp = new CsvServerResponseParser();
		return srp.parseResponseMessageListCustomerShowProposals(response);
	}



	private int getPort() {
		return 8080;
	}

	private String getAddress() {
		return SERVERIP;
	}

}
