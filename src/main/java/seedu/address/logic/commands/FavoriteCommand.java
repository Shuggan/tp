package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Favorites a Person in the Address Book
 */
public class FavoriteCommand extends Command {

    public static final String COMMAND_WORD = "fav";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Favourites the person according "
            + "to the Contact ID provided by user input.\n "
            + "Parameters: INDEX (must be positive integer).\n"
            + "Example: " + COMMAND_WORD + "1";

    public static final String MESSAGE_NOT_EXISTS = "Person doesn't exist in the address book";

    public static final String MESSAGE_ISFAVORITED = "Person is already favorited in this address book: %1s";

    public static final String MESSAGE_FAVORITE_PERSON_SUCCESS = "Favourited Person: %1$s";

    public final Index index;

    public FavoriteCommand(Index index) {
        this.index = index;
    }

    /**
     * Returns a CommandResult where the Selected Person
     * is Favorited.
     */
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_NOT_EXISTS);
        }

        Person personToFav = lastShownList.get(index.getZeroBased());

        if (personToFav.getIsFavorite().getFavoriteStatus()) {
            return new CommandResult(String.format(MESSAGE_ISFAVORITED, personToFav));
        }

        Person favoritedPerson = personToFav.favorite();

        model.setPerson(personToFav, favoritedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_FAVORITE_PERSON_SUCCESS, favoritedPerson));
    }
}
