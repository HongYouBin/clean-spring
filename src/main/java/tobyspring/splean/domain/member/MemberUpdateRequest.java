package tobyspring.splean.domain.member;

record MemberUpdateRequest(
        String nickname,
        String profileAddress,
        String introduction
) {
}
