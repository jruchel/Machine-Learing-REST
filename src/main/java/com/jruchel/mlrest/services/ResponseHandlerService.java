package com.jruchel.mlrest.services;

import com.jruchel.mlrest.models.User;
import com.jruchel.mlrest.models.dto.TrainingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class ResponseHandlerService {

    private final ModelService modelService;
    private final MailingService mailingService;

    public TrainingResult handleLinearRegressionTrainingResponse(TrainingResult response, boolean save, String saveName, User user) throws MessagingException {
        if (save) {
            if (response.getFile() != null) {
                modelService.save(response.toModel(saveName, user));
                if (modelService.findPrincipalModelByName(saveName) != null) {
                    response.setFile(saveName);
                    sendNotificationEmail(user, saveName, true);
                } else {
                    response.setFile("not saved");
                    sendNotificationEmail(user, saveName, false);
                }
            }
        }
        return response;
    }



    protected void sendNotificationEmail(User user, String saveName, boolean success) throws MessagingException {
        if (success)
            mailingService.sendEmail(
                    user.getEmail(),
                    "Training results",
                    String.format("Your model '%s' has finished training, you can now access it by going to your models.", saveName)
            );
        else
            mailingService.sendEmail(
                    user.getEmail(),
                    "Training results",
                    String.format("An error occured while training your model '%s', please try again.", saveName)
            );

    }
}
