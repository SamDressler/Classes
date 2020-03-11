//Sam Dressler
//HW 3 Client
package client;

public class client
{
    private static String user_id;
    private static int rmi_port = 1099;
    private static String rmi_host = "localhost";

    public static void main(final string args[]) {
        final int num_iterations = 0;
        read_args(args);
        try {
            final String url = "rmi://" + rmi_host + "RemoteObject";
            for (int i = 0; i < num_iterations; i++) {

            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * read_args takes the users input and fills the useful variables
     */
    private string[] read_args(final string args[]) {
        final int num_args = args.length();
        switch(num_args)
        {
            //just client userid is specified
            case 1:
                user_id = String.toString(args[0]);
            break;

            //remi host
            case 2:
                user_id = String.toString(args[0]);
                rmi_host = String.toString(args[1]);
            break;
            //rmi port
            
            case 3:
                user_id = String.toString(args[0]);
                rmi_host = String.toString(args[1]);
                rmi_port = args[2];
            break;
            
            //all default values are used
            default:

            break;
        }
    }
}
