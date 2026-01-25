package lapr4.integrations.plugins.dronelanguage.version_0_666;

import lapr4.integrations.dronelanguageplugin.application.DroneLanguageImporter;
import lapr4.integrations.dronelanguageplugin.dto.DroneLanguage;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.InputStream;

public class DroneLanguagePluginFormatImporter implements DroneLanguageImporter {

    @Override
    public DroneLanguage importFrom(InputStream inputStream) throws IOException {
        try {
            final var charStream = CharStreams.fromStream(inputStream);
            final var lexer = new DroneLanguagePluginLexer(charStream);
            final var tokens = new CommonTokenStream(lexer);
            final var parser = new DroneLanguagePluginParser(tokens);

            final ParseTree tree = parser.program();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                throw new IOException("Invalid Drone Language format");
            }

            final var visitor = new DroneLanguagePluginImplVisitor();
            visitor.visit(tree);

            return visitor.droneLanguagePlugin();

        } catch (Exception e) {
            throw new IOException("Invalid Drone Language format");
        }
    }
}