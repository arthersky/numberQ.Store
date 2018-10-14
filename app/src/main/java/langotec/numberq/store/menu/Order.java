package langotec.numberq.store.menu;
import android.view.OrientationEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Order extends ArrayList<Menu> implements Serializable {

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

    public Order(){}
    public Order(
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
            String comment/*,
            String userName,
            Calendar orderDT*/){
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
        /*setOrderDT(orderDT);*/
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
    }

}
