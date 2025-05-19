package tr.com.my_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pin_aciklama")
@Getter
@Setter
public class PinAciklama {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pin_id", nullable = false)
    private Pin pin;

    @Column(name = "dil", length = 10, nullable = false)
    private String dil; // 'tr', 'en', 'de', ...

    @Column(name = "aciklama", length = 255, nullable = false)
    private String aciklama;
}
