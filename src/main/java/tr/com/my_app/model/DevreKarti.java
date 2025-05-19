    package tr.com.my_app.model;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.Collection;
    import java.util.List;

    @Entity
    @Table(name = "devre_karti")
    @Getter
    @Setter
    public class DevreKarti {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "adi", length = 255)
        private String adi;

        @OneToMany(mappedBy = "devreKarti", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        private List<DevreKartiAciklama> aciklamalar;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "devre_karti_pin",
                joinColumns = @JoinColumn(name = "devre_karti_id"),
                inverseJoinColumns = @JoinColumn(name = "pin_id")
        )
        private List<Pin> pinler;

        @OneToMany(mappedBy = "devreKarti", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<PinConfig> konfigler;

    }
