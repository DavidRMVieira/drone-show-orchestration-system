package lapr4.integrations.plugins.proposaltemplate.version_1_0;

import java.io.IOException;
import java.io.InputStream;

import lapr4.integrations.proposaltemplate.application.ProposalTemplateImporter;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ProposalTemplateFormatImporter implements ProposalTemplateImporter {

    @Override
    public String importFrom(InputStream inputStream) throws IOException {
        final var charStream = CharStreams.fromStream(inputStream);
        final var lexer = new ProposalTemplateLexer(charStream);
        final var tokens = new CommonTokenStream(lexer);
        final var parser = new ProposalTemplateParser(tokens);

//        parser.removeErrorListeners();
//        lexer.removeErrorListeners();
//        parser.addErrorListener(new BaseErrorListener() {
//            @Override
//            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
//                                    int line, int charPositionInLine, String msg,
//                                    RecognitionException e) {
//            }
//        });

        final ParseTree tree = parser.proposal();

        // Handle parsing errors
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new IOException("Invalid proposal template format");
        }

        final var listener = new ProposalTemplateImplListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);

        return listener.generateDocument();
    }
}