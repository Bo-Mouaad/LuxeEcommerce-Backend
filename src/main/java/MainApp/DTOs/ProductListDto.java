package MainApp.DTOs;

public interface ProductListDto {
    Long getProductId();
    String getName();
    double getPrice();
    String getImageUrl();
    double getRating();
    boolean getIsNew();
    boolean getInStock();
    CategoryInfo getCategory();

    interface CategoryInfo {
        String getName();
    }
}
