package com.eshop.common.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45 , unique = true)
    private String name;

    @Column(nullable = false , length = 45)
    private String logo;

    @ManyToMany
    @JoinTable(name = "brands_categories" ,
                joinColumns = @JoinColumn (name = "brand_id") ,
                inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Brand() {
    }

    public Brand(Integer id) {
        this.id = id;
    }

    public Brand(String name) {
        this.name = name;
    }

    public Brand(Integer id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        String cate = "";
        for (Category category : categories) {
            cate+= category.getName() + " , ";
        }
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", categories=" + cate +
                '}';
    }

    @Transient
    public String getLogoPath() {
        if(this.id == null) return "/images/logo-not-found.png";

        return "/brand-logos/" + this.id + "/" + this.logo;
    }
}
