package kz.edu.astanait.application.controllers;

import kz.edu.astanait.application.exceptions.ConvertException;
import kz.edu.astanait.application.exceptions.IncomeException;
import kz.edu.astanait.application.exceptions.TransferException;
import kz.edu.astanait.application.models.ConvertMoneyDTO;
import kz.edu.astanait.application.models.IncomeMoneyDTO;
import kz.edu.astanait.application.models.MoneyTransferDTO;
import kz.edu.astanait.application.models.User;
import kz.edu.astanait.application.services.interfaces.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CardController {
    private final ICardService cardService;

    @Autowired
    public CardController(ICardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create-card")
    public String createCard(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        cardService.createCard(user);

        model.addAttribute("title", "Card is created");
        model.addAttribute("message", "Card has created successfully");
        return "index";
    }

    @GetMapping("/transfer")
    public String showTransferMoneyPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        model.addAttribute("moneyTransferDTO", new MoneyTransferDTO());
        model.addAttribute("userCards", cardService.findByUserId(user.getId()));
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(@ModelAttribute("moneyTransferDTO") MoneyTransferDTO moneyTransferDTO,
                                HttpServletRequest request,
                                Model model) {
        User user = (User) request.getSession().getAttribute("user");
        try {
            cardService.transferMoney(moneyTransferDTO, user);
        } catch (TransferException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("moneyTransferDTO", new MoneyTransferDTO());
            model.addAttribute("userCards", cardService.findByUserId(user.getId()));
            return "transfer";
        }
        return "redirect:/profile";
    }

    @PostMapping("/convert")
    public String convertMoney(@ModelAttribute ConvertMoneyDTO convertMoneyDTO,
                               HttpServletRequest request,
                               Model model){
        try {
            cardService.convertMoney(convertMoneyDTO);
        } catch (ConvertException e) {
            User user = (User) request.getSession().getAttribute("user");
            model.addAttribute("userCards", cardService.findByUserId(user.getId()));
            model.addAttribute("convertMoneyDTO",new ConvertMoneyDTO());
            model.addAttribute("errorMessage", e.getMessage());
            return "convertMoney";
        }

        return "redirect:/profile";
    }

    @GetMapping("/convert-money")
    public String convertMoneyPage(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("userCards", cardService.findByUserId(user.getId()));
        model.addAttribute("convertMoneyDTO",new ConvertMoneyDTO());
        return "convertMoney";
    }

    @PostMapping("/income")
    public String setIncome(@ModelAttribute IncomeMoneyDTO incomeMoneyDTO,
                            HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        try {
            cardService.makeIncome(incomeMoneyDTO, user);
        } catch (IncomeException e) {
            model.addAttribute("userCards", cardService.findByUserId(user.getId()));
            model.addAttribute("incomeMoneyDTO",new IncomeMoneyDTO());
            model.addAttribute("errorMessage", e.getMessage());
            return "getMoney";
        }
        return "redirect:/profile";
    }

    @GetMapping("/write-income")
    public String getIncome(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("userCards", cardService.findByUserId(user.getId()));
        model.addAttribute("incomeMoneyDTO",new IncomeMoneyDTO());

        return "getMoney";
    }
}