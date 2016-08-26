package com.nostra13.universalimageloader.sample.behaviors.validate;

import com.nostra13.universalimageloader.sample.behaviors.validate.media.ValidateMediaFormatBehavior;
import com.nostra13.universalimageloader.sample.utils.MediaFilesUtils;

/**
 * Created by Mor on 26/08/2016.
 */
public class ValidateAudioFormatBehavior implements ValidateMediaFormatBehavior {
    @Override
    public boolean isValidFormatByLink(String link) {
        return MediaFilesUtils.isValidAudioFormat(link);
    }
}
