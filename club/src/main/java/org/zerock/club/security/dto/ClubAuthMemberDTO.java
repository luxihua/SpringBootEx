package org.zerock.club.security.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
// User 클래스를 상속받고
public class ClubAuthMemberDTO extends User {

    private String email;

    private String name;

    private boolean fromSocial;


    // 부모 클래스인 User 클래스의 생성자를 호출할 수 있는 코드 생성
//    public ClubAuthMemberDTO(
//            String username,
//            String password,
//            boolean fromSocial,
//            Collection<? extends GrantedAuthority> authorities) {
//
//        super(username, password, authorities);
//        this.email = username;
//        this.fromSocial= fromSocial;
//    }

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.fromSocial = fromSocial;

    }

}
