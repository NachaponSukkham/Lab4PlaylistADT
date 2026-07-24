import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**

* Playlist — ADT แทนรายการเพลงที่ผู้ใช้จัดลำดับไว้
*
* ค่านามธรรม (A): ลำดับของเพลง เช่น [เพลงA, เพลงB, เพลงC]
*
* ตัวอย่างการใช้งาน:
* ```
  Playlist p = new Playlist();
  ```
* ```
  p.add("Bohemian Rhapsody");
  ```
* ```
  p.add("Imagine");
  ```
* ```
  System.out.println(p.size());   // 2
  ```

*/
public class Playlist {


public static final int MAX_SONGS = 100;

// ===== representation =====

/**
 * Abstraction Function:
 * AF(songs) = รายการเพลงที่ผู้ใช้จัดเรียงไว้สำหรับเปิดฟัง
 * โดยเพลงที่อยู่ลำดับแรกจะเป็นเพลงที่เล่นก่อน
 * และเพลงถัดไปจะเล่นต่อไปตามลำดับที่ปรากฏอยู่ในรายการ
 *
 * Representation Invariant:
 * - songs ต้องมีอยู่จริงและต้องไม่เป็น null
 * - ทุกเพลงในรายการต้องมีชื่อเพลงอยู่จริงและต้องไม่เป็น null
 * - ชื่อเพลงต้องไม่เป็นสตริงว่าง
 * - ในเพลย์ลิสต์เดียวกันต้องไม่มีชื่อเพลงซ้ำกัน
 * - จำนวนเพลงทั้งหมดต้องไม่เกิน MAX_SONGS เพลง
 *
 * Safety from rep exposure:
 * - เมื่อสร้าง Playlist จาก List ที่รับมาจากภายนอก
 *   จะคัดลอกข้อมูลมาเก็บใน ArrayList ใหม่เสมอ
 *   เพื่อป้องกันการแก้ไขจากภายนอก
 * - เมธอด songs() จะคืนสำเนาของรายการเพลง
 *   ไม่คืนตัวแปร songs โดยตรง
 *   จึงไม่สามารถแก้ไขข้อมูลภายใน Playlist ได้จากภายนอก
 */
private final List<String> songs;

/**
 * ตรวจสอบ Representation Invariant
 */
private void checkRep() {

    assert songs != null
            : "songs ต้องไม่เป็น null";

    assert songs.size() <= MAX_SONGS
            : "จำนวนเพลงต้องไม่เกิน MAX_SONGS";

    Set<String> seen = new HashSet<>();

    for (String s : songs) {

        assert s != null
                : "พบเพลงเป็น null";

        assert !s.isEmpty()
                : "พบชื่อเพลงเป็นสตริงว่าง";

        assert seen.add(s)
                : "ชื่อเพลงซ้ำ: " + s;
    }
}

// ===== Creator =====

/**
 * สร้างเพลย์ลิสต์ว่าง
 */
public Playlist() {
    this.songs = new ArrayList<>();
    checkRep();
}

/**
 * สร้างเพลย์ลิสต์จากรายชื่อเพลงที่ให้มา
 *
 * @param initial รายชื่อเพลงเริ่มต้น
 * @throws IllegalArgumentException ถ้าข้อมูลผิดเงื่อนไข
 */
public Playlist(List<String> initial) {

    if (initial == null) {
        throw new IllegalArgumentException(
                "initial must not be null");
    }

    if (initial.size() > MAX_SONGS) {
        throw new IllegalArgumentException(
                "too many songs");
    }

    Set<String> seen = new HashSet<>();

    for (String s : initial) {

        if (s == null) {
            throw new IllegalArgumentException(
                    "song must not be null");
        }

        if (s.isEmpty()) {
            throw new IllegalArgumentException(
                    "song must not be empty");
        }

        if (!seen.add(s)) {
            throw new IllegalArgumentException(
                    "duplicate song: " + s);
        }
    }

    this.songs = new ArrayList<>(initial);

    checkRep();
}

// ===== Mutators =====

/**
 * เพิ่มเพลงต่อท้ายเพลย์ลิสต์
 *
 * @param song ชื่อเพลง
 * @return true ถ้าเพิ่มสำเร็จ
 */
public boolean add(String song) {

    if (song == null) {
        throw new IllegalArgumentException(
                "song must not be null");
    }

    if (song.isEmpty()) {
        throw new IllegalArgumentException(
                "song must not be empty");
    }

    if (songs.contains(song)) {
        return false;
    }

    if (songs.size() >= MAX_SONGS) {
        return false;
    }

    songs.add(song);

    checkRep();

    return true;
}

/**
 * ลบเพลงออกจากเพลย์ลิสต์
 *
 * @param song ชื่อเพลง
 * @return true ถ้าลบสำเร็จ
 */
public boolean remove(String song) {

    boolean removed = songs.remove(song);

    if (removed) {
        checkRep();
    }

    return removed;
}

// ===== Observers =====

/**
 * คืนจำนวนเพลงในเพลย์ลิสต์
 */
public int size() {
    return songs.size();
}

/**
 * ตรวจว่ามีเพลงนี้อยู่หรือไม่
 */
public boolean contains(String song) {
    return songs.contains(song);
}

/**
 * คืนรายชื่อเพลงทั้งหมดตามลำดับ
 *
 * ระวัง: ห้ามคืน songs ตรง ๆ
 */
public List<String> songs() {
    return new ArrayList<>(songs);
}

// ===== Producer =====

/**
 * คืน Playlist ใหม่ที่มีเพลงชุดเดิม
 * แต่สลับลำดับการจัดเรียง
 */
public Playlist shuffled() {

    List<String> copy = new ArrayList<>(songs);

    Collections.shuffle(copy);

    return new Playlist(copy);
}

@Override
public String toString() {
    return songs.toString();
}

}
