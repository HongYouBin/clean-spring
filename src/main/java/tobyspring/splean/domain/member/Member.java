    package tobyspring.splean.domain.member;

    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Entity;
    import jakarta.persistence.FetchType;
    import jakarta.persistence.OneToOne;
    import lombok.AccessLevel;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.ToString;
    import org.hibernate.annotations.NaturalId;
    import org.hibernate.annotations.NaturalIdCache;
    import org.springframework.util.Assert;
    import tobyspring.splean.domain.AbstractEntity;
    import tobyspring.splean.domain.shared.Email;

    import static java.util.Objects.requireNonNull;

    @Getter
    @Entity
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @NaturalIdCache
    @ToString(callSuper = true, exclude = "detail")
    public class Member extends AbstractEntity {
        @NaturalId
        private Email email;

        private String nickname;

        private String passwordHash;

        private MemberStatus status;

        private MemberDetail detail;

        public void activate() {
            Assert.state(this.status == MemberStatus.PENDING, "가입 대기중인 상태에서만 가입 완료가 가능합니다.");

            this.status = MemberStatus.ACTIVATE;
            detail.activate();
        }

        public void deactivate() {
            Assert.state(this.status == MemberStatus.ACTIVATE, "가입이 완료된 상태에서만 탈퇴가 가능합니다.");

            this.status = MemberStatus.DEACTIVATED;
            this.detail.deactivate();
        }

        public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordEncoder) {
            Member member = new Member();

            member.email = new Email(requireNonNull(createRequest.email()));
            member.nickname = requireNonNull(createRequest.nickname());
            member.passwordHash = passwordEncoder.encode(requireNonNull(createRequest.password()));
            member.status = MemberStatus.PENDING;

            member.detail = MemberDetail.create();

            return member;
        }

        public void changeNickname(String nickname) {
            this.nickname = nickname;
        }

        public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
            return passwordEncoder.matches(password, this.passwordHash);
        }

        public void changePassowrd(String password, PasswordEncoder passwordEncoder) {
            this.passwordHash = passwordEncoder.encode(password);
        }

        public void updateInfo(MemberUpdateRequest memberUpdateRequest){
            this.nickname = memberUpdateRequest.nickname();
            this.detail.updateInfo(memberUpdateRequest);
        }
    }
