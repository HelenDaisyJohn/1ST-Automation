package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import pojo.CreateDecoder;
import pojo.UpdateDecoder;
import pojo.DisplayName;

import java.io.IOException;

public class VideoDecoder {
    public String createDecoderPayload(String Parent, String DisplayName, String Vendor, String DecoderIdentifier, String SupportedChannelVendor, String VendorChannelIdentifier, String ProjectID, String Zone, String InstanceName) throws IOException {
        CreateDecoder cd = new CreateDecoder();
        cd.setParent(Parent);
        DisplayName name = new DisplayName();
        name.setValue(DisplayName);
        cd.setDisplay_name(name);
        cd.setVendor("VENDOR_LTN");
        cd.setState("STATE_UNSPECIFIED");
        cd.setVendor_decoder_identifier(DecoderIdentifier);
        cd.setSupported_channel_vendor("VENDOR_PHENIX");
        cd.setVendor_channel_identifier(VendorChannelIdentifier);
        cd.setProject_id(ProjectID);
        cd.setZone(Zone);
        cd.setInstance_name(InstanceName);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(cd);
    }

    public String updateDecoderPayload(String DisplayName, String DecoderIdentifier, String ProjectID, String Zone, String InstanceName, String VendorChannelIdentifier) throws IOException {
        UpdateDecoder ud = new UpdateDecoder();
        DisplayName name = new DisplayName();
        name.setValue(DisplayName);
        ud.setDisplay_name(name);
        ud.setVendor_decoder_identifier(DecoderIdentifier);
        ud.setVendor_channel_identifier(VendorChannelIdentifier);
        ud.setState("STATE_IDLE");
        ud.setSupported_channel_vendor("VENDOR_PHENIX");
        ud.setProject_id(ProjectID);
        ud.setZone(Zone);
        ud.setInstance_name(InstanceName);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(ud);
    }
}
