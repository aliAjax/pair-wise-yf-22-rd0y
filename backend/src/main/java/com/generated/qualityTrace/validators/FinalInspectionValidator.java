package com.generated.qualityTrace.validators;

import com.generated.qualityTrace.constants.ErrorCodes;
import com.generated.qualityTrace.constants.ErrorMessages;
import com.generated.qualityTrace.types.FinalInspectionSubmission;
import com.generated.qualityTrace.utils.ServiceException;

public final class FinalInspectionValidator {
  public static void validate(FinalInspectionSubmission req) {
    if (req == null
        || req.batchNo() == null || req.batchNo().isBlank()
        || req.gaugeCode() == null || req.gaugeCode().isBlank()) {
      throw new ServiceException(ErrorCodes.VALIDATION_FAILED, ErrorMessages.VALIDATION_FAILED);
    }
  }
}
