package tr.com.my_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "pinConfig")
@Getter
@Setter
public class PinConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "adi", length = 255)
    private String adi;

    @Column(name = "pinValues", length = 512)
    private String pinValues;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "olusturulma_tarihi")
    private Date olusturulmaTarihi;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devre_karti_id", nullable = false)
    private DevreKarti devreKarti;
}
