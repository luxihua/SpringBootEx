# springsecurity 사용

## 프로젝트를 위한 JPA 처리

1. ClubMember(회원) Entity 구성
    - 이메일
    - 패스워드
    - 이름
    - 소셜 가입 여부
    - 기타(등록일/수정일)

2. ClubMemberRole(권한) 구성
    * 회원 권한은 굳이 Entity를 설정하지 말고 @ElementCollection 어노테이션을 이용해서 구성 하는 것이 효율적임
    - USER : 일반 회원
    - MANAGER : 중간 관리 회원
    - ADMIN : 총괄 관리자


3. 필요한 추가 사항

    - @EnalbeJpaAuditing 어노테이션 추가
     -> ClubMember에 여러 권한을 가질 수 있게함
     
     - ClubMemberRole 클래스에 enum 타입으로 USER, MANAGER, ADMIN 추가
     -> 권한을 지정해줌
     
     - ClubMember 클래스에 ClubMemberRole 타입값 처리
     -> Set 타입을 이용하여 roleSet 컬렉션 추가
     
     
     - ClubMember 클래스 
     
     ``` Java
     package org.zerock.club.entity;


     import jakarta.persistence.ElementCollection;
     import jakarta.persistence.Entity;
     import jakarta.persistence.FetchType;
     import jakarta.persistence.Id;
     import lombok.*;

     import java.util.HashSet;
     import java.util.Set;

     @Entity
     @Builder
     @AllArgsConstructor
     @NoArgsConstructor
     @Getter
     @ToString
     public class ClubMember extends BaseEntity{

         @Id
         private String email;

         private String password;

         private String name;

         private boolean fromSocial;

         // ClubMemberRole 타입 처리를 위한 Set 타입 추가
         @ElementCollection(fetch = FetchType.LAZY)
         @Builder.Default
         private Set<ClubMemberRole> roleSet = new HashSet<>();

         public void addMemberRole(ClubMemberRole clubMemberRole) {
             roleSet.add(clubMemberRole);
         }

     }
```
