package io.wisoft.capstonedesign.thymeleaf;


import io.wisoft.capstonedesign.member.Member;
import io.wisoft.capstonedesign.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = Member.newInstance(form.getNickname(), form.getEmail(), form.getPassword(), form.getPhoneNumber());
        memberService.signUp(member);
        return "redirect:/";
    }

    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}

