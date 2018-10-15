package langotec.numberq.store.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MainOrder implements Serializable {

    private String id;
    private String orderId;
    private String userId;
    private String HeadId;
    private String BranchId;
    private String deliveryType;
    private String contactPhone;
    private String deliveryAddress;
    private String taxId;
    private String payWay;
    private int payCheck;
    private int totalPrice;
    private String comment;
    private String userName;
    private Calendar orderDT;
    private Calendar orderGetDT;
    private String HeadName;
    private String BranchName;
//    private int quantity;
//    private int sumprice;
    private String productType;
//    private String productName;
    private String image;
    private int available;
    private int waitingTime;
    private String description;

    private ArrayList<Menu> menuList;
    private ArrayList<String> productName, quantity, sumprice;

    public MainOrder(){

            menuList = new ArrayList<>();
            productName = new ArrayList<>();
            quantity = new ArrayList<>();
            sumprice = new ArrayList<>();

    }
    public MainOrder(
            String orderId,
            String userId,
            String HeadId,
            String BranchId,
            String deliveryType,
            String contactPhone,
            String deliveryAddress,
            String taxId,
            String payWay,
            int payCheck,
            int totalPrice,
            String comment,
            String userName,
            Calendar orderDT,
            Calendar orderGetDT,
            String HeadName,
            String BranchName,
            String productType,
            String image,
            int available,
            int waitingTime,
            String description){
        menuList = new ArrayList<>();
        productName = new ArrayList<>();
        quantity = new ArrayList<>();
        sumprice = new ArrayList<>();
        setOrderId(orderId);
        setUserId(userId);
        setHeadId(HeadId);
        setBranchId(BranchId);
        setDeliveryType(deliveryType);
        setContactPhone(contactPhone);
        setDeliveryAddress(deliveryAddress);
        setTaxId(taxId);
        setPayWay(payWay);
        setPayCheck(payCheck);
        setTotalPrice(totalPrice);
        setComment(comment);
        setUserName(userName);
        setOrderDT(orderDT);
        setOrderGetDT(orderGetDT);
        setHeadName(HeadName);
        setBranchName(BranchName);
        setQuantity(quantity);
        setSumprice(sumprice);
        setProductName(productName);
        setProductType(productType);
        setImage(image);
        setAvailable(available);
        setWaitingTime(waitingTime);
        setDescription(description);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadId() {
        return HeadId;
    }

    public void setHeadId(String headId) {
        HeadId = headId;
    }

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int isPayCheck() {
        return payCheck;
    }

    public void setPayCheck(int payCheck) {
        this.payCheck = payCheck;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Calendar getOrderDT() {
        return orderDT;
    }

    public String getOrderDT(String str){
        return String.valueOf(
                "下訂日期" + orderDT.get(Calendar.YEAR) +
                        "/" + setDigit(orderDT.get(Calendar.MONTH)) +
                        "/" + setDigit(orderDT.get(Calendar.DAY_OF_MONTH)) +
                        "   時間" + setDigit(orderDT.get(Calendar.HOUR_OF_DAY)) +
                        ":" + setDigit(orderDT.get(Calendar.MINUTE)) +
                        ":" + setDigit(orderDT.get(Calendar.SECOND)));
    }

    private String setDigit(int str){
        String value = String.valueOf(str);
        return value.length() == 2 ? value : "0"+value;
    }

    public void setOrderDT(Calendar orderDT) {
        this.orderDT = orderDT;
//        Log.e("Calendar Parse","Calendar:"+orderDT.get(Calendar.SECOND));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPayCheck() {
        return payCheck;
    }

    public Calendar getOrderGetDT() {
        return orderGetDT;
    }

    public void setOrderGetDT(Calendar orderGetDT) {
        this.orderGetDT = orderGetDT;
    }

    public String getHeadName() {
        return HeadName;
    }

    public void setHeadName(String headName) {
        HeadName = headName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<Menu> menuList) {
        this.menuList = menuList;
    }

    public ArrayList<String> getProductName() {
        return productName;
    }

    public void setProductName(ArrayList<String> productName) {
        this.productName = productName;
    }

    public ArrayList<String> getQuantity() {
        return quantity;
    }

    public void setQuantity(ArrayList<String> quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getSumprice() {
        return sumprice;
    }

    public void setSumprice(ArrayList<String> sumprice) {
        this.sumprice = sumprice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
