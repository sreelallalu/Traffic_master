package appcmpt.example.Traffic.n;

/**
 * Created by lalu on 4/7/2017.
 */
public class DataUserAge {
    private static DataUserAge  dataObject = null;

    private DataUserAge () {
        // left blank intentionally
    }

    public static DataUserAge  getInstance() {
        if (dataObject == null)
            dataObject = new DataUserAge ();
        return dataObject;
    }
    private String distributor_id;;

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {

        this.distributor_id = distributor_id;
    }
}
