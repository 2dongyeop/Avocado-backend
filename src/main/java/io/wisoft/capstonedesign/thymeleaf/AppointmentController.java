package io.wisoft.capstonedesign.thymeleaf;

import io.wisoft.capstonedesign.domain.appointment.application.AppointmentService;
import io.wisoft.capstonedesign.domain.hospital.application.HospitalService;
import io.wisoft.capstonedesign.domain.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AppointmentController {

    private final MemberService memberService;
    private final HospitalService hospitalService;
    private final AppointmentService appointmentService;

//    @GetMapping(value = "/appointment")
//    public String createForm(Model model) {
//        System.out.println("AppointmentController.createForm");
//
//        List<Member> memberList = memberService.findAll();
//        List<Hospital> hospitalList = hospitalService.findAll();
//
//        model.addAttribute("members", memberList);
//        model.addAttribute("hospitals", hospitalList);
//        model.addAttribute("hospitalDepts", HospitalDept.toList());
//
//        return "appointment/appointmentForm";
//    }
//
//    @PostMapping(value = "/appointment")
//    public String appointment(
//            @RequestParam("memberId") Long memberId,
//            @RequestParam("hospitalId") Long hospitalId,
//            @RequestParam("hospitalDeptId") HospitalDept dept,
//            @RequestParam("comment") String comment,
//            @RequestParam("appointName") String appointName,
//            @RequestParam("appointPhonenumber") String appointPhonenumber) {
//
//        appointmentService.save(memberId, hospitalId, dept, comment, appointName, appointPhonenumber);
//        return "redirect:/appointments";
//    }
//
//    @GetMapping(value = "/appointments")
//    public String appointmentList(
//            @ModelAttribute("appointmentSearch") AppointmentSearch appointmentSearch,
//            Model model) {
//
//        List<Appointment> appointments = appointmentService.findAllByCriteria(appointmentSearch);
//        model.addAttribute("appointments", appointments);
//
//        return "appointment/appointmentList";
//    }
//
//    @PostMapping(value = "/appointments/{appointmentId}/cancel")
//    public String cancelAppointment(@PathVariable("appointmentId") Long appointmentId) {
//
//        appointmentService.cancelAppointment(appointmentId);
//        return "redirect:/appointments";
//    }
}
