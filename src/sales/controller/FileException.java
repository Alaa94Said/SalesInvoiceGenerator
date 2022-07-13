/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales.controller;

/**
 *
 * @author Dahab
 */
public class FileException extends Exception{

        public FileException ()
        {
                super("this file format is not supported");
        }
}
