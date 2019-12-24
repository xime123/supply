package bd.com.supply.module.transaction.model.domian;

/**
 *
 */
public class SoSoAd {
    private int adPositionId;
    private int mediaType;
    private String name;
    private String link;
    private String imageUrl;
    private String content;
    private long endTime;
    private int enabled;

    public int getAdPositionId() {
        return adPositionId;
    }

    public void setAdPositionId(int adPositionId) {
        this.adPositionId = adPositionId;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "SoSoAd{" +
                "adPositionId=" + adPositionId +
                ", mediaType=" + mediaType +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", content='" + content + '\'' +
                ", endTime=" + endTime +
                ", enabled=" + enabled +
                '}';
    }
}
