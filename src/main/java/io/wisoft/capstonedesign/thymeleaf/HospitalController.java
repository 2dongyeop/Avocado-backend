package io.wisoft.capstonedesign.thymeleaf;

import io.wisoft.capstonedesign.hospital.Hospital;
import io.wisoft.capstonedesign.hospital.HospitalService;
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
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping(value = "/hospitals/new")
    public String createForm(Model model) {
        model.addAttribute("hospitalForm", new HospitalForm());
        return "hospitals/createHospitalForm";
    }

    @PostMapping(value = "/hospitals/new")
    public String create(@Valid HospitalForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "hospitals/createHospitalForm";
        }

        Hospital hospital = Hospital.createHospital(form.getName(), form.getNumber(), form.getAddress(), form.getOperatingTime());

        hospitalService.save(hospital);
        return "redirect:/";
    }

    @GetMapping(value = "/hospitals")
    public String list(Model model) {
        List<Hospital> hospitals = hospitalService.findAll();
        model.addAttribute("hospitals", hospitals);
        return "hospitals/hospitalList";
    }
}
