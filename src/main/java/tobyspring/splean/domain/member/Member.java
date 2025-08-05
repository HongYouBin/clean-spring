    package tobyspring.splean.domain.member;

    import jakarta.persistence.Entity;
    import lombok.AccessLevel;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.ToString;
    import org.hibernate.annotations.NaturalId;
    import org.hibernate.annotations.NaturalIdCache;
    import tobyspring.splean.domain.AbstractEntity;
    import tobyspring.splean.domain.shared.Email;

    import static java.util.Objects.requireNonNull;
    import static org.springframework.util.Assert.state;

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
            state(this.status == MemberStatus.PENDING, "가입 대기중인 상태에서만 가입 완료가 가능합니다.");

            this.status = MemberStatus.ACTIVATE;
            detail.activate();
        }

        public void deactivate() {
            state(this.status == MemberStatus.ACTIVATE, "가입이 완료된 상태에서만 탈퇴가 가능합니다.");

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

        public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
            return passwordEncoder.matches(password, this.passwordHash);
        }

        public void changePassowrd(String password, PasswordEncoder passwordEncoder) {
            this.passwordHash = passwordEncoder.encode(password);
        }

        public void updateInfo(MemberUpdateRequest memberUpdateRequest){
//            state(this.getStatus() != MemberStatus.ACTIVATE, "member 상태가 activate여야 합니다.");
            if(this.getStatus() != MemberStatus.ACTIVATE) {
                throw new IllegalStateException("member 상태가 activate여야 합니다.");
            }
            this.nickname = memberUpdateRequest.nickname();
            this.detail.updateInfo(memberUpdateRequest);
        }
    }
