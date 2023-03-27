package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE_TIME;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.editeventcommand.EventDescriptor;
import seedu.address.logic.parser.editpersoncommandsparser.PersonDescriptor;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.OneTimeEvent;
import seedu.address.model.event.RecurringEvent;
import seedu.address.model.event.fields.DateTime;
import seedu.address.model.event.fields.Description;
import seedu.address.model.event.fields.Recurrence;
import seedu.address.model.person.Person;
import seedu.address.model.person.fields.*;

import java.util.List;

public class EditEventCommand extends Command {
    public static final String COMMAND_WORD = "editevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an event in the event list. "
            + "Parameters: "
            + "INDEX "
            + PREFIX_START_DATE_TIME + "START DATE TIME "
            + PREFIX_END_DATE_TIME + "END DATE TIME"
            + "[" + PREFIX_RECURRENCE + "INTERVAL] "
            + "Example: " + COMMAND_WORD + " "
            + "2 "
            + PREFIX_START_DATE_TIME + "2023-03-10 16:00 "
            + PREFIX_END_DATE_TIME + "2023-03-10 18:00"
            + PREFIX_RECURRENCE + "weekly ";

    private static final String MESSAGE_EDIT_EVENT_SUCCESS = "Event edited: %1$s";
    private static final String MESSAGE_INVALID_EVENT = "This event does not exist in the Calendar!";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    private static final String MESSAGE_INVALID_INTERVAL = "END DATE TIME ("
            + PREFIX_END_DATE_TIME + ") should be after START DATE TIME ("
            + PREFIX_START_DATE_TIME + ")";

    protected final EventDescriptor editEventDescriptor;
    private final Index index;

    /**
     * @param editEventDescriptor details to edit the event with
     */
    public EditEventCommand(EventDescriptor editEventDescriptor) {
        requireNonNull(editEventDescriptor);

        this.index = editEventDescriptor.getIndex().get();
        this.editEventDescriptor = new EventDescriptor(editEventDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        // TODO
        requireNonNull(model);
        List<Event> eventList = model.getEvents();

        if (index.getZeroBased() >= eventList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Event eventToEdit = eventList.get(index.getZeroBased());
        Event editedEvent = createEditedEvent(eventToEdit, editEventDescriptor);

        if (!model.hasEvent(eventToEdit)) {
            throw new CommandException(MESSAGE_INVALID_EVENT);
        }

        model.setEvent(eventToEdit, editedEvent);
        // model.updateEventList (Need to do)
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedEvent));
    }

    public EventDescriptor getEditEventDescriptor() {
        return this.editEventDescriptor;
    }

    protected static Event createEditedEvent(Event eventToEdit, EventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        Description Description = editEventDescriptor.getDescription().orElse(eventToEdit.getDescription());
        DateTime startDateTime = editEventDescriptor.getStartDateTime().orElse(eventToEdit.getStartDateTime());
        DateTime endDateTime = editEventDescriptor.getEndDateTime().orElse(eventToEdit.getEndDateTime());
        Recurrence recurrence = editEventDescriptor.getRecurrence().orElse(eventToEdit.getRecurrence());

        if (recurrence.isRecurring()) {
            return new RecurringEvent(Description, startDateTime, endDateTime, recurrence);
        } else {
            return new OneTimeEvent(Description, startDateTime, endDateTime);
        }
    }

    /**
     * Returns true if the two events have the same identity and data fields.
     * This defines a weaker notion of equality between two events.
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        EditEventCommand otherCommand = (EditEventCommand) other;
        return editEventDescriptor.equals(otherCommand.editEventDescriptor);
    }
}
