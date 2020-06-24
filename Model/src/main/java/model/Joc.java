package model;

import model.Entity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@javax.persistence.Entity
@Table(name="Jocuri")
public class Joc extends model.Entity<Integer> implements Serializable, Comparable<Joc> {
    private String jucator1;
    private String jucator2;
    private Integer pozitie1;
    private Integer pozitie2;
    private String castigator;

    public Joc(){

    }

    public Joc(String jucator1, String jucator2, Integer pozitie1, Integer pozitie2) {
        this.jucator1 = jucator1;
        this.jucator2 = jucator2;
        this.pozitie1 = pozitie1;
        this.pozitie2 = pozitie2;
    }

    @Override
    public String toString() {
        return "Joc{" +
                "jucator1='" + jucator1 + '\'' +
                ", jucator2='" + jucator2 + '\'' +
                ", pozitie1=" + pozitie1 +
                ", pozitie2=" + pozitie2 +
                ", castigator='" + castigator + '\'' +
                '}';
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public void setId(Integer integer) {
        super.setId(integer);
    }

    public String getJucator1() {
        return jucator1;
    }

    public void setJucator1(String jucator1) {
        this.jucator1 = jucator1;
    }

    public String getJucator2() {
        return jucator2;
    }

    public void setJucator2(String jucator2) {
        this.jucator2 = jucator2;
    }

    public Integer getPozitie1() {
        return pozitie1;
    }

    public void setPozitie1(Integer pozitie1) {
        this.pozitie1 = pozitie1;
    }

    public Integer getPozitie2() {
        return pozitie2;
    }

    public void setPozitie2(Integer pozitie2) {
        this.pozitie2 = pozitie2;
    }

    public String getCastigator() {
        return castigator;
    }

    public void setCastigator(String castigator) {
        this.castigator = castigator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joc joc = (Joc) o;
        return Objects.equals(jucator1, joc.jucator1) &&
                Objects.equals(jucator2, joc.jucator2) &&
                Objects.equals(pozitie1, joc.pozitie1) &&
                Objects.equals(pozitie2, joc.pozitie2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jucator1, jucator2, pozitie1, pozitie2);
    }

    @Override
    public int compareTo(Joc o) {
        return this.getId().compareTo(o.getId());
    }
}
