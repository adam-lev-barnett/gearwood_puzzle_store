/*
import edu.barnett.music_api.entities.Album;
import edu.barnett.music_api.entities.Artist;
import edu.barnett.music_api.repositories.AlbumRepository;
import edu.barnett.music_api.repositories.ArtistRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MusicDataInitializer {
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    public MusicDataInitializer(ArtistRepository artistRepository, AlbumRepository albumRepository) {
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }
    @PostConstruct
    public void init() {
        if (artistRepository.count() != 0 || albumRepository.count() != 0) {
            System.out.println("Data already present - not executing initalizer");
            return;
        }
        System.out.println("Initializing test data");
        Artist taylor = new Artist("Taylor Swift", "Pop", "Reading, PA");
        Artist kendrick = new Artist("Kendrick Lamar", "Hip-Hop", "Compton, CA");
        Artist coldplay = new Artist("Coldplay", "Alternative Rock", "London, UK");
        Artist adele = new Artist("Adele", "Soul/Pop", "Tottenham, London");
        Artist drake = new Artist("Drake", "Hip-Hop", "Toronto, Canada");
        Artist beyonce = new Artist("Beyoncé", "R&B/Pop", "Houston, TX");
        Artist vnv = new Artist("VNV Nation", "Electronic/Industrial", "London/Dublin");
        Artist rammstein = new Artist("Rammstein", "Industrial Metal", "Berlin, Germany");
        Album album1 = new Album("1989", 2014, 13); // Taylor Swift
        Album album2 = new Album("Folklore", 2020, 16);
        Album album3 = new Album("Midnights", 2022, 13);
        Album album4 = new Album("Good Kid, M.A.A.D City", 2012, 12); // Kendrick Lamar
        Album album5 = new Album("To Pimp a Butterfly", 2015, 16);
        Album album6 = new Album("DAMN.", 2017, 14);
        Album album7 = new Album("Parachutes", 2000, 10); // Coldplay
        Album album8 = new Album("A Rush of Blood to the Head", 2002, 11);
        Album album9 = new Album("A Head Full of Dreams", 2015, 11);
        Album album10 = new Album("19", 2008, 12); // Adele
        Album album11 = new Album("21", 2011, 11);
        Album album12 = new Album("30", 2021, 12);
        Album album13 = new Album("Take Care", 2011, 17); // Drake
        Album album14 = new Album("Nothing Was the Same", 2013, 13);
        Album album15 = new Album("Scorpion", 2018, 25);
        Album album16 = new Album("Dangerously in Love", 2003, 15); // Beyoncé
        Album album17 = new Album("Lemonade", 2016, 12);
        Album album18 = new Album("Renaissance", 2022, 16);
        Album album19 = new Album("Empires", 1999, 10); // VNV Nation
        Album album20 = new Album("Futureperfect", 2002, 11);
        Album album21 = new Album("Of Faith, Power and Glory", 2009, 9);
        Album album22 = new Album("Sehnsucht", 1997, 11); // Rammstein
        Album album23 = new Album("Mutter", 2001, 11);
        Album album24 = new Album("Zeit", 2022, 11);
// Add albums to the artists
        taylor.addAlbum(album1);
        taylor.addAlbum(album2);
        taylor.addAlbum(album3);
        kendrick.addAlbum(album4);
        kendrick.addAlbum(album5);
        kendrick.addAlbum(album6);
        coldplay.addAlbum(album7);
        coldplay.addAlbum(album8);
        coldplay.addAlbum(album9);
        adele.addAlbum(album10);
        adele.addAlbum(album11);
        adele.addAlbum(album12);
        drake.addAlbum(album13);
        drake.addAlbum(album14);
        drake.addAlbum(album15);
        beyonce.addAlbum(album16);
        beyonce.addAlbum(album17);
        beyonce.addAlbum(album18);
        vnv.addAlbum(album19);
        vnv.addAlbum(album20);
        vnv.addAlbum(album21);
        rammstein.addAlbum(album22);
        rammstein.addAlbum(album23);
        rammstein.addAlbum(album24);
// Save artists (cascade saves albums)
        artistRepository.saveAll(
                Arrays.asList(
                        taylor, kendrick, coldplay,
                        adele, drake, beyonce,
                        vnv, rammstein));
    }
}

 */
