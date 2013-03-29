package commandline.parsing.HelpClasses;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.Operation;
import commandline.parsing.parser.SwitchSetParser;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:33 AM
 */
public class RemoteFileOperationDelete extends Operation<RemoteFileSwitchesForDelete, RemoteFileOperationDelete.Params> {
    static public class Params implements RemoteFileSwitchesForDelete {
        private String fileToDelete;
        private String server;
        private boolean verbose;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Params params = (Params) o;

            if (verbose != params.verbose) return false;
            if (fileToDelete != null ? !fileToDelete.equals(params.fileToDelete) : params.fileToDelete != null) return false;
            if (server != null ? !server.equals(params.server) : params.server != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        public Params(String server, String fileToDelete, boolean verbose) {
            this.fileToDelete = fileToDelete;
            this.server = server;
            this.verbose = verbose;
        }

        @Override
        public String getFileToDelete() {
            return fileToDelete;
        }

        @Override
        public String getRemoteServer() {
            return server;
        }

        @Override
        public Boolean getVerboseLog() {
            return verbose;
        }
    }
    public RemoteFileOperationDelete() throws ParsingException {
        super(RemoteFileOperationNames.delete, new SwitchSetParser<RemoteFileSwitchesForDelete>(RemoteFileSwitchesForDelete.class));
    }

    @Override
    public Params onOperation(RemoteFileSwitchesForDelete set) {
        return new Params(set.getRemoteServer(), set.getFileToDelete(), set.getVerboseLog());
    }
}
