package Model;

import Pojo.Mac;

import java.util.List;

public class TDest {
//                {
//                    'dest':'4号楼',
//                        'longitude':'1234.1',
//                        'latitude':'1222.1',
//                        'macs':
//                                    [
//                    {
//                        'mac':'11111111',
//                            'db':'23',
//                    },
//                    {
//                        'mac':'22',
//                            'db':'33',
//                    }
//                                    ]
//                },
    long did;
    String dest;
    double longitude;
    double latitude;
    List<Mac> macs;

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<Mac> getMacs() {
        return macs;
    }

    public void setMacs(List<Mac> macs) {
        this.macs = macs;
    }
}
