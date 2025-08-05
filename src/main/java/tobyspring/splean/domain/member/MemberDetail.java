    package tobyspring.splean.domain.member;

    import jakarta.persistence.Entity;
    import lombok.AccessLevel;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.ToString;
    import org.springframework.util.Assert;
    import tobyspring.splean.domain.AbstractEntity;

    import java.time.LocalDateTime;

    @Getter
    @Entity
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString(callSuper = true)
    public class MemberDetail extends AbstractEntity {
        private Profile profile;

        private String introduction;

        private LocalDateTime registeredAt;

        private LocalDateTime activatedAt;

        private LocalDateTime deactivatedAt;

        static MemberDetail create() {
            MemberDetail memberDetail = new MemberDetail();
            memberDetail.registeredAt = LocalDateTime.now();

            return memberDetail;
        }

        void activate() {
            Assert.isTrue(activatedAt == null, "이미 activate 되었습니다.");
            activatedAt = LocalDateTime.now();
        }

        void deactivate() {
            Assert.isTrue(deactivatedAt == null, "이미 deactivate 되었습니다.");
            deactivatedAt = LocalDateTime.now();
        }

        void updateInfo(MemberUpdateRequest memberUpdateRequest) {
            this.profile = new Profile(memberUpdateRequest.profileAddress());
            this.introduction = memberUpdateRequest.introduction();
        }
    }
