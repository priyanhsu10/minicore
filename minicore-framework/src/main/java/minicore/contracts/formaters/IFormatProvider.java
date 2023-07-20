package minicore.contracts.formaters;

public interface IFormatProvider {
  boolean canSuportedInputMediaType(String mediaType);

  boolean canSuportedOutpMediaType(String mediaType);

  IInputFormatter getSuportedInputFormatter(String context);

  IOutputFormatter getSupportedOutputFormatter(String context);
}
