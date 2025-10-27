/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.cines;

import db.CinesDB;
import java.util.List;
import models.cines.Cine;

/**
 *
 * @author andy
 */
public class CinesCrudServices {
    public List<Cine> getAllCines() {
        CinesDB cinesDB = new CinesDB();

        //return eventsDb.getAllEvents();
        return cinesDB.getAllCines();
    }
}
