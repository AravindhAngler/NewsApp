package app.news.com.newsapp.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HighlightResult {

    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("url")
    @Expose
    private Url url;
    @SerializedName("author")
    @Expose
    private Autor author;
    @SerializedName("story_text")
    @Expose
    private StoryText storyText;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public Autor getAuthor() {
        return author;
    }

    public void setAuthor(Autor author) {
        this.author = author;
    }

    public StoryText getStoryText() {
        return storyText;
    }

    public void setStoryText(StoryText storyText) {
        this.storyText = storyText;
    }

}
