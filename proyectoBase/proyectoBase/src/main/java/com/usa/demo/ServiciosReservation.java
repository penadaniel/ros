/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.usa.demo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
@Service
public class ServiciosReservation implements Serializable{
    @Autowired
    private RepositorioReservation metodosCrud;
    
    public List<Reservation> getAll(){
         return metodosCrud.getAll();
    }
    
    public Optional<Reservation> getReservation(int idReservation){
        return metodosCrud.getReservation(idReservation);
    }
    
    
    public Reservation save(Reservation reservations){
        if(reservations.getIdReservation()==null){
            return metodosCrud.save(reservations);
        }else{
            Optional<Reservation> evt=metodosCrud.getReservation(reservations.getIdReservation());
            if(!evt.isPresent()){
            return metodosCrud.save(reservations);
            }else{
                return reservations;
            }
        
        
        }
    }
    public Reservation update(Reservation reservations){
        if(reservations.getIdReservation()!=null){
            Optional<Reservation> evt= metodosCrud.getReservation(reservations.getIdReservation());
            if(evt.isPresent()){

                if(reservations.getStartDate()!=null){
                    evt.get().setStartDate(reservations.getStartDate());
                }
                if(reservations.getDevolutionDate()!=null){
                    evt.get().setDevolutionDate(reservations.getDevolutionDate());
                }
                if(reservations.getStatus()!=null){
                    evt.get().setStatus(reservations.getStatus());
                }
                metodosCrud.save(evt.get());
                return evt.get();
            }else{
                return reservations;
            }
        }else{
            return reservations;
        }
    }

    public boolean deleteReservation(int reservationId) {
        Boolean aBoolean = getReservation(reservationId).map(reservations -> {
            metodosCrud.delete(reservations);
            return true;
        }).orElse(false);
        return aBoolean;
    }
    
    public StatusReserva reporteStatusServicio (){
        List<Reservation>completed= metodosCrud.ReservacionStatusRepositorio("completed");
        List<Reservation>cancelled= metodosCrud.ReservacionStatusRepositorio("cancelled");
        return new StatusReserva(completed.size(), cancelled.size() );
    }
    
    public List<Reservation> reporteTiempoServicio (String datoA, String datoB){
        SimpleDateFormat parser = new SimpleDateFormat ("yyyy-MM-dd");
        
        Date datoUno = new Date();
        Date datoDos = new Date();
        
        try{
             datoUno = parser.parse(datoA);
             datoDos = parser.parse(datoB);
        }catch(ParseException evt){
            evt.printStackTrace();
        }if(datoUno.before(datoDos)){
            return metodosCrud.ReservacionTiempoRepositorio(datoUno, datoDos);
        }else{
            return new ArrayList<>();
        
        } 
    } 
 
    public List<ContadorClient> reporteClientesServicio(){
            return metodosCrud.getRepositorioClient();
        }
    
}
