# Domain Glossary

**TEA -> Terms, Expressions, and Acronyms**

-----

- ### User Aggregate

| **_TEA_ (EN)**   | **_Description_ (EN)**                                                                      |
|:-----------------|:--------------------------------------------------------------------------------------------|
| **EmailAddress** | Represents the user's email address.                                                        |
| **PhoneNumber**  | Represents the user's phone number.                                                         |
| **Role**         | Defines the role or function of the user within the system.                                 |
| **ShowOurUser**  | Represents a system user, containing information such as username, password, and real name. |


- ### Customer Aggregate

| **_TEA_ (EN)**      | **_Description_ (EN)**                                                      |
|:--------------------|:----------------------------------------------------------------------------|
| **Address**         | Represents the customer's address.                                          |
| **Customer**        | Represents a customer who interacts with the system.                        |
| **CustomerState**   | Enumeration that defines the current state of the customer.                 |
| **Position**        | Represents the position held by the representative within the organization. |
| **Representative**  | Represents a customer’s representative.                                     |
| **VAT**             | Represents the customer's tax identification number.                        |


- ### Show Aggregate

| **_TEA_ (EN)**     | **_Description_ (EN)**                                                         |
|:-------------------|:-------------------------------------------------------------------------------|
| **Date**           | Represents the date of the show.                                               |
| **Duration**       | Represents the length of time for which the show runs.                         |
| **Place**          | Represents the location where the show takes place.                            |
| **Result**         | Enumeration that defines the outcome of the simulation.                        |
| **Show**           | Represents an event or performance.                                            |
| **SimulationLog**  | Represents a record of a simulated execution of a show.                        |


- ### Figure Aggregate

| **_TEA_ (EN)**      | **_Description_ (EN)**                                                                |
|:--------------------|:--------------------------------------------------------------------------------------|
| **Figure**          | Represents a figure that is part of a show.                                           |
| **FigureCode**      | Represents a unique code associated with the figure.                                  |
| **FigureType**      | Enumeration that defines the type of the figure.                                      |
| **FigureVersion**   | Represents the version of the figure.                                                 |
| **Description**     | Value object that provides textual description of the figure.                         |
| **DSLDescription**  | Represents the domain-specific language (DSL) description associated with the figure. |
| **DSLId**           | Represents a unique identifier for the DSL.                                           |
| **DSLType**         | Enumeration that defines the type of DSL.                                             |
| **DSLVersion**      | Represents the version of the DSL.                                                    |


- ### FigureInShow Aggregate

| **_TEA_ (EN)**      | **_Description_ (EN)**                                       |
|:--------------------|:-------------------------------------------------------------|
| **FigureInShow**    | Represents a figure instance that is part of a show.         |


- ### Drone Aggregate

| **_TEA_ (EN)**      | **_Description_ (EN)**                                   |
|:--------------------|:---------------------------------------------------------|
| **AcquisitionDate** | Represents the date the drone was acquired.              |
| **Drone**           | Represents a drone used in the shows.                    |
| **DroneState**      | Enumeration that defines the current state of the drone. |
| **SerialNumber**    | Represents the drone’s serial number.                    |


- ### DroneModel Aggregate

| **_TEA_ (EN)**     | **_Description_ (EN)**                                      |
|:-------------------|:------------------------------------------------------------|
| **DroneModel**     | Represents the model of a drone.                            |
| **Name**           | Represents the name of a drone model or manufacturer.       |
| **Manufacturer**   | Represents the entity that manufactures the drone.          |


- ### DroneInShow Aggregate

| **_TEA_ (EN)**     | **_Description_ (EN)**                               |
|:-------------------|:-----------------------------------------------------|
| **DroneInShow**    | Represents a drone instance participating in a show. |


- ### ShowRequest Aggregate

| **_TEA_ (EN)**    | **_Description_ (EN)**                                     |
|:------------------|:-----------------------------------------------------------|
| **Date**          | Represents the requested date for the show.                |
| **Description**   | Represents a detailed description of the request.          |
| **Duration**      | Represents the duration of the show.                       |
| **NumberDrones**  | Represents the number of drones required for the show.     |
| **Place**         | Represents the location where the show will take place.    |
| **RequestState**  | Enumeration that defines the current state of the request. |
| **ShowRequest**   | Represents a request for a show to be performed.           |


- ### ShowProposal Aggregate

| **_TEA_ (EN)**         | **_Description_ (EN)**                                          |
|:-----------------------|:----------------------------------------------------------------|
| **Date**               | Represents the proposed date for the show.                      |
| **Duration**           | Represents the duration proposed for the show.                  |
| **Place**              | Represents the proposed location for the show.                  |
| **ProposalValidation** | Enumeration that defines the validation status of the proposal. |
| **ShowProposal**       | Represents a show proposal based on a request.                  |


-----

- ### Important Relationships

  - **ShowOurUser** can have one or more **Role**.

  - **Customer** can have one or more **Representative**.
  
  - **Representative** is associated with a **ShowOurUser**.
  
  - **Show** is associated with one or more **FigureInShow**, each of which represents a **Figure**.
  
  - **Show** features one or more **DroneInShow**, each of which represents a **Drone**.
  
  - **DSLDescription** defines a **DroneModel**.
  
  - **Drone** has a **DroneModel**.
  
  - **Figure** is optionally exclusive to a **Customer**.
  
  - **ShowRequest** is associated with a **Customer**.
  
  - **ShowProposal** is associated with a **ShowRequest** and one or more **FigureInShow**.
  
  - **SimulationLog** belongs to a **Show** and includes a **Date** and a **Result**.








