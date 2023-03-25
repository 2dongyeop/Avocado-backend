package io.wisoft.capstonedesign.thymeleaf;


import io.wisoft.capstonedesign.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

//    @GetMapping(value = "/members/new")
//    public String createForm(Model model) {
//        model.addAttribute("memberForm", new MemberForm());
//        return "members/createMemberForm";
//    }
//
//    @PostMapping(value = "/members/new")
//    public String create(@Valid MemberForm form, BindingResult result) {
//
//        if (result.hasErrors()) {
//            return "members/createMemberForm";
//        }
//
//        Member member = Member.newInstance(form.getNickname(), form.getEmail(), form.getPassword(), form.getPhoneNumber());
//        memberService.signUp(member);
//        return "redirect:/";
//    }
//
//    @GetMapping(value = "/members")
//    public String list(Model model) {
//        List<Member> members = memberService.findAll();
//        model.addAttribute("members", members);
//        return "members/memberList";
//    }
}

