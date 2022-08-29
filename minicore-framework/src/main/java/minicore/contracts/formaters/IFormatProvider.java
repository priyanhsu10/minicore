package minicore.contracts.formaters;

import minicore.contracts.HttpContext;

public interface IFormatProvider {
boolean canSuportedInputMediaType(String mediaType);
  boolean canSuportedOutpMediaType(String mediaType);
  IInputFormatter getSuportedInputFormatter(String context);
  IOutputFormatter getSupportedOutputFormatter(String context);
}
