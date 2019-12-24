package bd.com.walletdb.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class TransactionEntity {
    @Id(autoincrement = true)
    private Long id;
    private String from;
    private String to;
    private double value;
    private long  time;
    @Generated(hash = 673469102)
    public TransactionEntity(Long id, String from, String to, double value,
            long time) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.value = value;
        this.time = time;
    }
    @Generated(hash = 1319631883)
    public TransactionEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
}
