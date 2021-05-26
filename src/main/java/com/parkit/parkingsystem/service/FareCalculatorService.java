package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.util.ArrayList;
import java.util.List;



public class FareCalculatorService {

    private TicketDAO ticket;

    public void calculateFare(Ticket ticket, int nbEntry){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();


        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = (outHour - inHour) /(1000*60); // duration in minutes
        double ratio = duration/60; // coefficient for price calculation

        if (ratio>=0.5){ //durée >= 30 minutes
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    if (nbEntry>1){
                        ticket.setPrice(ratio * Fare.CAR_RATE_PER_HOUR * 0.95); //Discount 5% for recurring users
                    }else {
                        ticket.setPrice(ratio * Fare.CAR_RATE_PER_HOUR);
                    }
                    break;
                }
                case BIKE: {
                    if (nbEntry>1){
                        ticket.setPrice(ratio * Fare.BIKE_RATE_PER_HOUR * 0.95); //Discount 5% for recurring users
                    }else {
                        ticket.setPrice(ratio * Fare.BIKE_RATE_PER_HOUR);
                    }
                    break;
                }
                default: throw new IllegalArgumentException("Unkown Parking Type");
            }
        }else { // durée < 30 minutes
            ticket.setPrice(0);
        }


    }
}