package com.gelitix.backend.ticketType.controller;

import com.gelitix.backend.response.Response;
import com.gelitix.backend.ticketType.dto.CreateTicketTypeDto;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.service.TicketTypeService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RolesAllowed("ROLE_EVENT_ORGANIZER")
@RestController
@RequestMapping("api/v1/ticket-type")
public class TicketTypeController {
    private final TicketTypeService ticketTypeService;

    public TicketTypeController(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    @GetMapping("")
    public ResponseEntity<?> getTicketType() {
        ticketTypeService.getAllTicketTypes();
        return Response.success(200,"These are the ticket types", ticketTypeService.getAllTicketTypes());
      }

      @GetMapping("/{id}")
    public ResponseEntity<?> getTicketTypeById(@PathVariable Long id) {
          return Response.success(200,"These are the ticket types", ticketTypeService.getTicketTypeByIdResponse(id));
      }

      @PostMapping("")
    public ResponseEntity<?> createTicketType(@RequestBody CreateTicketTypeDto createTicketTypeDto, Long eventId) {

        ticketTypeService.createTicketType(createTicketTypeDto , eventId);
        return Response.success(200,"Ticket type created", ticketTypeService.getTicketTypeById(eventId));
      }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicketType (@PathVariable Long id) {
        ticketTypeService.deleteTicketType(id);
        return Response.success(200,"Ticket type deleted", ticketTypeService.getTicketTypeById(id));
    }


}
