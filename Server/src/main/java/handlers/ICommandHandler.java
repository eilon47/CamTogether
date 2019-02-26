package handlers;

import commands.CommandRequest;
import commands.CommandResponse;

public interface ICommandHandler {
    CommandResponse handle(CommandRequest request);
}
