package yt.corazonid.petakUmpet;

import org.bukkit.entity.Player;

import java.util.*;

public class QuizManager {
    private final Map<String, String> quizBank = new HashMap<>();
    private final Map<Player, String> activeQuestion = new HashMap<>();
    private final Map<Player, String> playerActiveQuestion = new HashMap<>();

    public QuizManager() {
        // --- 1-10: STARTER ---
        quizBank.put("Ibu kota Indonesia?", "Jakarta");
        quizBank.put("Hasil dari 15 x 4 x 3?", "180");
        quizBank.put("Bahasa pemrograman Minecraft?", "Java");
        quizBank.put("Warna kotak hitam pesawat?", "Oranye");
        quizBank.put("Planet ke-3 dari matahari?", "Bumi");
        quizBank.put("Apa nama mob peniru suara?", "Parrot");
        quizBank.put("Berapa blok tinggi pemain?", "2");
        quizBank.put("Siapa pencipta Minecraft?", "Notch");
        quizBank.put("Senjata baru update 1.21?", "Mace");
        quizBank.put("Mob pembawa Totem?", "Evoker");

        // --- 11-20: MINECRAFT & IT ---
        quizBank.put("Item bernapas dalam air?", "Conduit");
        quizBank.put("Jumlah obsidian portal nether?", "10");
        quizBank.put("Alat pindah enchant buku?", "Anvil");
        quizBank.put("Mob takut kucing?", "Creeper");
        quizBank.put("Singkatan Random Access Memory?", "RAM");
        quizBank.put("Singkatan Central Processing Unit?", "CPU");
        quizBank.put("Bahasa pemrograman logo ular?", "Python");
        quizBank.put("Nama default karakter wanita?", "Alex");
        quizBank.put("Blok tak hancur di survival?", "Bedrock");
        quizBank.put("Berapa jumlah slot shulker?", "27");

        // --- 21-40: MATEMATIKA ---
        quizBank.put("Akar pangkat dua dari 144?", "12");
        quizBank.put("Berapakah 2 pangkat 5?", "32");
        quizBank.put("Hasil dari 12 x 12?", "144");
        quizBank.put("Hasil dari 100 dibagi 4?", "25");
        quizBank.put("Jumlah sudut dalam segitiga?", "180");
        quizBank.put("Hasil dari 25 + 25 x 2?", "75");
        quizBank.put("Hasil dari 10 - (-5)?", "15");
        quizBank.put("Berapakah 15 persen dari 200?", "30");
        quizBank.put("Hasil dari 1/2 + 1/2?", "1");
        quizBank.put("Berapakah 9 x 9?", "81");
        quizBank.put("Angka romawi dari 10?", "X");
        quizBank.put("Berapa jumlah sisi pada kubus?", "6");
        quizBank.put("Hasil dari 500 - 125?", "375");
        quizBank.put("Berapa detik dalam satu jam?", "3600");
        quizBank.put("Hasil dari 3 x 3 x 3?", "27");
        quizBank.put("Akar pangkat dua dari 625?", "25");
        quizBank.put("Hasil dari 81 dibagi 9?", "9");
        quizBank.put("Berapa titik sudut lingkaran?", "0");
        quizBank.put("Berapakah 11 x 11?", "121");
        quizBank.put("Hasil dari 7 x 8?", "56");

        // --- 41-60: PENGETAHUAN UMUM & LOGIKA ---
        quizBank.put("Logam cair di termometer?", "Raksa");
        quizBank.put("Hewan darat tercepat?", "Cheetah");
        quizBank.put("Negara penduduk terbanyak?", "Cina");
        quizBank.put("Apa nama benua terkecil?", "Australia");
        quizBank.put("Zat hijau daun?", "Klorofil");
        quizBank.put("Gunung tertinggi di dunia?", "Everest");
        quizBank.put("Negara matahari terbit?", "Jepang");
        quizBank.put("Jumlah provinsi Indonesia?", "38");
        quizBank.put("Lautan terluas di dunia?", "Pasifik");
        quizBank.put("Bulan dengan 28 hari?", "Semua");
        quizBank.put("Mata uang Amerika?", "Dollar");
        quizBank.put("Mamalia terbesar di laut?", "Paus");
        quizBank.put("Benua tempat piramida berada?", "Afrika");
        quizBank.put("Warna primer merah dan biru?", "Ungu");
        quizBank.put("Alat pernapasan ikan?", "Insang");
        quizBank.put("Taring gajah disebut?", "Gading");
        quizBank.put("Pencipta lampu pijar?", "Edison");
        quizBank.put("Berapa tahun dalam satu dekade?", "10");
        quizBank.put("Berapa kaki laba-laba?", "8");
        quizBank.put("Arah matahari terbenam?", "Barat");

        // --- 61-80: SAINS & ALAM ---
        quizBank.put("Sebutkan planet merah?", "Mars");
        quizBank.put("Organ pemompa darah?", "Jantung");
        quizBank.put("Suhu air membeku celcius?", "0");
        quizBank.put("Planet terbesar tata surya?", "Jupiter");
        quizBank.put("Pusat tata surya kita?", "Matahari");
        quizBank.put("Nama proses makan tumbuhan?", "Fotosintesis");
        quizBank.put("Satelit alami bumi?", "Bulan");
        quizBank.put("Awan terbuat dari?", "Air");
        quizBank.put("Alat pengukur gempa?", "Seismograf");
        quizBank.put("Jumlah warna pelangi?", "7");
        quizBank.put("Penyebab karat pada besi?", "Oksigen");
        quizBank.put("Energi panas bumi?", "Geotermal");
        quizBank.put("Lambang kimia air?", "H2O");
        quizBank.put("Hewan air dan darat?", "Amfibi");
        quizBank.put("Bintang terdekat dari bumi?", "Matahari");
        quizBank.put("Lama bumi rotasi (jam)?", "24");
        quizBank.put("Burung tak bisa terbang?", "Pinguin");
        quizBank.put("Zat cair di gunung berapi?", "Magma");
        quizBank.put("Indra manusia untuk melihat?", "Mata");
        quizBank.put("Tulang pelindung otak?", "Tengkorak");

        // --- 81-100: SEJARAH & BUDAYA ---
        quizBank.put("Apa dasar negara Indonesia?", "Pancasila");
        quizBank.put("Candi terbesar di Indonesia?", "Borobudur");
        quizBank.put("Kerajaan Hindu pertama?", "Kutai");
        quizBank.put("Senjata tradisional Jateng?", "Keris");
        quizBank.put("Tanggal Sumpah Pemuda (Oktober)?", "28");
        quizBank.put("Tarian khas Bali?", "Kecak");
        quizBank.put("Tahun PD2 berakhir?", "1945");
        quizBank.put("Penjajah Indonesia terlama?", "Belanda");
        quizBank.put("Danau terbesar di Indonesia?", "Toba");
        quizBank.put("Pahlawan emansipasi wanita?", "Kartini");
        quizBank.put("Jembatan Surabaya Madura?", "Suramadu");
        quizBank.put("Makanan khas Yogyakarta?", "Gudeg");
        quizBank.put("Tahun Indonesia merdeka?", "1945");
        quizBank.put("Nama presiden pertama Indonesia?", "Soekarno");
        quizBank.put("Rumah adat Papua?", "Honai");
        quizBank.put("Monumen nasional di Jakarta?", "Monas");
        quizBank.put("Alat musik khas Sunda?", "Angklung");
        quizBank.put("Lagu kebangsaan Indonesia?", "Indonesia");
        quizBank.put("Pulau dewata adalah sebutan?", "Bali");
        quizBank.put("Bahan utama pembuatan cokelat?", "Kakao");
    }

    public String getRandomQuestion() {
        List<String> questions = new ArrayList<>(quizBank.keySet());
        return questions.get(new Random().nextInt(questions.size()));
    }

    public void setActiveQuestion(Player p, String q) { playerActiveQuestion.put(p, q); }
    public boolean hasActiveQuestion(Player p) { return playerActiveQuestion.containsKey(p); }
    public String getActiveQuestion(Player p) { return playerActiveQuestion.get(p); }
    public void removeActiveQuestion(Player p) { playerActiveQuestion.remove(p); }

    public boolean checkAnswer(String question, String answer) {
        return quizBank.get(question).equalsIgnoreCase(answer);
    }
}
