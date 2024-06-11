package resources;

public enum APIResources {
	AddEncoderAPI("/prism/v1/video/videoEncoder"),
	GetEncoderAPI("/prism/v1/video/{name}"),
	DeleteEncoderAPI("/prism/v1/video/{name}"),
	ListEncoderAPI("/prism/v1/video/videoEncoders"),
	UpdateEncoderAPI("/prism/v1/video"),
	DisableEncoderAPI("/prism/v1/video"),
	ReenableEncoderAPI("/prism/v1/video");
//	DisableEncoderAPI("/prism/v1/video/{name}:disable"),
//	ReenableEncoderAPI("/prism/v1/video/{name}:reenable");
		
	private String resource;
	
	APIResources(String resource)
	{
		this.resource=resource;
	}
	public String getResource()
	{
		return resource;
	}

}


