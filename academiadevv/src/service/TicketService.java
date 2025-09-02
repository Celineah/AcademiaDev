package service;

import model.TicketSuporte;

import java.util.LinkedList;
import java.util.Queue;

public class TicketService {
    private Queue<TicketSuporte> fila = new LinkedList<>();

    public void abrirTicket(TicketSuporte ticket) {
        fila.add(ticket);
    }

    public TicketSuporte atenderProximo() {
        return fila.poll(); 
    }

    public boolean temTicketsPendentes() {
        return !fila.isEmpty();
    }

    public int getTotalPendentes() {
        return fila.size();
    }
}
