/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.ghostpassword.ghostpasswordbackend;

/**
 * Holding class for the password service. This is probably not the best idea, but we only have a few hours left.
 * @author udeyoje
 */
public class PasswordServiceHolder
{
    private static PasswordService serviceInstance = null;
    
    public static void setPasswordService(PasswordService service){
        serviceInstance = service;
    }
    
    public static PasswordService getPasswordService(){
        return serviceInstance;
    }
    
    
}
