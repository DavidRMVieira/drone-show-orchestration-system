package lapr4.integrations.plugins.dsl.version_1_1_23;

import java.io.IOException;
import java.io.InputStream;

import lapr4.integrations.dslplugin.dto.DSLDescription;
import lapr4.integrations.dslplugin.application.DSLImporter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class DSLPluginFormatImporter implements DSLImporter {

    @Override
    public DSLDescription importFrom(InputStream inputStream) throws IOException {
        try {
            final var charStream = CharStreams.fromStream(inputStream);
            final var lexer = new DSLPluginLexer(charStream);
            final var tokens = new CommonTokenStream(lexer);
            final var parser = new DSLPluginParser(tokens);

            final ParseTree tree = parser.program();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                throw new IOException("Invalid DSL format");
            }

            final var visitor = new DSLPluginImplVisitor();
            visitor.visit(tree);

            return visitor.DSLPlugin();

        } catch (Exception e) {
            throw new IOException("Invalid DSL format");
        }
    }
}
