package com.mark.moveandswipervitem;

/**
 * Created by mark on 18-8-11.
 */
public class ImageModel {

    private long id;
    private int imageWidth;
    private int imageHeight;
    private String imagePath;
    private String imageName;
    private String imageTitle;

    public ImageModel() {
    }

    public ImageModel(String imagePath, String imageName, String imageTitle) {
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.imageTitle = imageTitle;
    }

    public ImageModel(int imageWidth, int imageHeight, String imagePath, String imageName, String imageTitle) {
        if (imageWidth == 0){
            imageWidth = 480;
        }if (imageHeight == 0){
            imageHeight = 640;
        }
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.imageTitle = imageTitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        if (imageWidth == 0){
            imageWidth = 480;
        }
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        if (imageHeight == 0){
            imageHeight = 640;
        }
        this.imageHeight = imageHeight;
    }
}
