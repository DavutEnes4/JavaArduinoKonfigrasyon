package tr.com.my_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "devre_karti_aciklama")
@Getter
@Setter
public class DevreKartiAciklama {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "devre_id", nullable = false)
    private DevreKarti devreKarti;

    @Column(name = "dil", length = 10, nullable = false)
    private String dil; // 'tr', 'en', 'de', ...

    @Column(name = "aciklama", length = 255, nullable = false)
    private String aciklama;
}
