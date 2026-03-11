package com.explore.securityApp.job;

import com.explore.securityApp.entity.Booking;
import com.explore.securityApp.enums.BookingStatus;
import com.explore.securityApp.repository.BookingRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExpireBookingJob implements Job {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void execute(JobExecutionContext context){

        String bookingCode = context.getMergedJobDataMap().getString("bookingCode");

        Booking booking = bookingRepository.findByBookingCode(bookingCode).orElseThrow(null);

        if (booking != null && booking.getStatus().equals(BookingStatus.PENDING)){
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);
        }
    }
}
