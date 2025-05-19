package tr.com.my_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore; // Bunu ekle

import java.util.List;

@Entity
@Table(name = "pin")
@Getter @Setter
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adi", length = 255)
    private String adi;

    @Column(name = "aciklamasi", length = 255)
    private String aciklamasi;

    @ManyToMany(mappedBy = "pinler", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DevreKarti> devreKartlari;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PinAciklama> aciklamalar;
}
