package commandline.parsing.HelpClasses;

import commandline.parsing.Exceptions.ParsingException;
import commandline.parsing.Operation;
import commandline.parsing.parser.SwitchSetParser;

/**
 * User: Chris
 * Date: 3/27/13
 * Time: 12:28 AM
 */
public class RemoteFileOperationCopy extends Operation<RemoteFileSwitchesForCopy, RemoteFileOperationCopy.Params> {
    public RemoteFileOperationCopy() throws ParsingException {
        super(RemoteFileOperationNames.copy, new SwitchSetParser<RemoteFileSwitchesForCopy>(RemoteFileSwitchesForCopy.class));
    }

    static public class Params implements RemoteFileSwitchesForCopy {
        private String server;
        private String srcFile;
        private String dstFile;
        private boolean verbose;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Params params = (Params) o;

            if (verbose != params.verbose) return false;
            if (!dstFile.equals(params.dstFile)) return false;
            if (!server.equals(params.server)) return false;
            if (!srcFile.equals(params.srcFile)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        public Params(String server, String srcFile, String dstFile, boolean verbose) {

            this.server = server;
            this.srcFile = srcFile;
            this.dstFile = dstFile;
            this.verbose = verbose;
        }

        @Override
        public String getFileToCopy() {
            return srcFile;
        }

        @Override
        public String getFileCopyTo() {
            return dstFile;
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

    @Override
    public Params onOperation(RemoteFileSwitchesForCopy set) {
        return new Params(set.getRemoteServer(), set.getFileToCopy(), set.getFileCopyTo(), set.getVerboseLog());
    }
}
