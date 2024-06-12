package resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pojo.CreateEncoder;
import pojo.DisplayName;
import pojo.UpdateEncoder;

public class VideoEncoder {
	public CreateEncoder createEncoderPayload(String Parent, String DisplayName, String EncoderIdentifier, String Vendor)
	{
		CreateEncoder ce=new CreateEncoder();
		ce.setParent(Parent);
		DisplayName name=new DisplayName();
		name.setValue(DisplayName);
		ce.setDisplay_name(name);
//		ce.setDisplayName(name);
		ce.setVendor_encoder_identifier(EncoderIdentifier);
		ce.setVendor("VENDOR_LTN");
		ce.setState("STATE_UNSPECIFIED");
		return ce;
	}
	public UpdateEncoder updateEncoderPayload(String DisplayName, String EncoderIdentifier) {
        UpdateEncoder ue = new UpdateEncoder();
        DisplayName name=new DisplayName();
        name.setValue(DisplayName);
        ue.setDisplay_name(name);
        ue.setVendor_encoder_identifier(EncoderIdentifier);
        return ue;
    }

}
